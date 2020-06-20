/*
 * Copyright (C) 2016-2020 Dennis Neufeld
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
import { User } from "@/components/user/user";

export const FETCH_USER = "FETCH_USER";
export const LOG_OUT = "LOG_OUT";

const LOAD_USER = "LOAD_USER";
const UNLOAD_USER = "UNLOAD_USER";
const FETCHING_USER = "FETCHING_USER";
const LOAD_FAILED = "LOAD_FAILED";

const defaultUser = new User(69, "User McUserFace", null, "0420");

export const userStore = {
	namespaced: true,
	modules: {},
	state: () => ({
		userLoading: false, //true each time there is a request for the user in flight
		userLoaded: false, //true as soon as we received the data for the first time, false after logout
		user: defaultUser,
	}),
	getters: {},
	mutations: {
		[LOAD_USER](state, user) {
			state.userLoading = false;
			state.user = user;
			state.userLoaded = true;
		},
		[UNLOAD_USER](state) {
			state.userLoaded = false;
			state.user = defaultUser;
		},
		[FETCHING_USER](state) {
			state.userLoading = true;
		},
		[LOAD_FAILED](state) {
			state.userLoading = false;
		},
	},
	actions: {
		async [FETCH_USER](context) {
			context.commit(FETCHING_USER);
			const response = await fetch("/public/user");
			if (response.status === 200) {
				let user = await response.json();
				context.commit(LOAD_USER, new User(user.discordId, user.name, user.avatarId, user.discriminator));
			} else {
				context.commit(LOAD_FAILED);
			}
		},
		async [LOG_OUT](context) {
			let csrfToken = document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*=\s*([^;]*).*$)|^.*$/, "$1");
			await fetch("/public/login", {
				method: "DELETE",
				headers: {
					"X-XSRF-TOKEN": csrfToken,
				},
			});
			context.commit(UNLOAD_USER);
		},
	},
};
