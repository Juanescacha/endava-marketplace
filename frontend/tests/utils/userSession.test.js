import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";
import { setActivePinia } from "pinia";
import { createTestingPinia } from "@pinia/testing";
import {
	logInUser,
	saveUserInfoFromServerToStore,
	logoutUser,
	saveUserInfoFromStoreToCookies,
	saveUserInfoFromCookiesToStore,
	userInfoIsInCookies,
	userIsLogedIn,
} from "@/utils/userSession.js";
import * as cookies from "@/utils/cookies";
import * as axios from "@/utils/axios";
import { useUserStore } from "@/stores/user.js";

const LOGIN_TOKEN_NAME = "access_token";
const USER_COOKIE_NAME = "user";
const mockToken =
	"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
const mockUser = {
	id: 1,
	name: "John",
	email: "John@email.com",
	admin: true,
};

beforeEach(() => {
	const pinia = createTestingPinia({ createSpy: vi.fn() });
	setActivePinia(pinia);
});

afterEach(() => {
	document.cookie = `${LOGIN_TOKEN_NAME}=1; expires=1 Jan 1970 00:00:00 GMT;`;
	document.cookie = `${USER_COOKIE_NAME}=1; expires=1 Jan 1970 00:00:00 GMT;`;
});

describe("logInUser", () => {
	it("should store the session token in cookies", () => {
		expect(document.cookie).toBe("");
		logInUser(mockToken);
		expect(document.cookie).toContain(mockToken);
	});

	// it("should call saveUserInfoFromServerToStore", async () => {
	// 	vi.spyOn(axios, "postUser").mockImplementationOnce(
	// 		() => mockUser
	// 	);
	// 	const fnSpy = vi.spyOn(
	// 		{ saveUserInfoFromServerToStore },
	// 		"saveUserInfoFromServerToStore"
	// 	);

	// 	await logInUser(mockToken);

	// 	expect(fnSpy).toHaveBeenCalled();
	// });
});

describe("saveUserInfoFromServerToStore", () => {
	it("should store the user info in the user store", async () => {
		const user = useUserStore();
		vi.spyOn(axios, "postUser").mockImplementationOnce(() => mockUser);

		await saveUserInfoFromServerToStore();

		expect(user.id).toBe(mockUser.id);
		expect(user.name).toBe(mockUser.name);
		expect(user.email).toBe(mockUser.email);
		expect(user.isAdmin).toBe(mockUser.admin);
	});

	it("should not throw error when server does not respond", async () => {
		const user = useUserStore();
		vi.spyOn(axios, "postUser").mockImplementationOnce(() => null);

		await saveUserInfoFromServerToStore();
		expect(user.id).toBe(0);
	});
});

describe("saveUserInfoFromStoreToCookies", () => {
	it("should not save the user info in cookies if there's no user in the store", () => {
		saveUserInfoFromStoreToCookies();
		expect(cookies.getCookie(USER_COOKIE_NAME)).toBeUndefined();
	});

	it("should create a user cookie", () => {
		const user = useUserStore();
		user.id = mockUser.id;
		user.name = mockUser.name;
		user.email = mockUser.email;
		saveUserInfoFromStoreToCookies();
		expect(cookies.getCookie(USER_COOKIE_NAME)).toContain(user.name);
	});
});

describe("saveUserInfoFromCookiesToStore", () => {
	it("should populate the user store", () => {
		const user = useUserStore();
		vi.spyOn(cookies, "getCookie").mockImplementationOnce(
			() =>
				'{"id":1,"name":"John","email":"John@email.com","isAdmin":false}'
		);
		saveUserInfoFromCookiesToStore();
		expect(user.id).toBe(1);
	});

	it("should leave the default user store if no user cookie is found", () => {
		const user = useUserStore();
		saveUserInfoFromCookiesToStore();
		expect(user.id).toBe(0);
	});
});

describe("userIsLogedIn", () => {
	it("should call a method to get cookies", () => {
		const getCookieSpy = vi.spyOn(cookies, "getCookie");
		userIsLogedIn(LOGIN_TOKEN_NAME);
		expect(getCookieSpy).toHaveBeenCalled();
	});

	it("should return true when the user session cookie is set", () => {
		vi.spyOn(cookies, "getCookie").mockImplementationOnce(() => {
			const obj = {};
			obj[LOGIN_TOKEN_NAME] = "jwt";
			return obj;
		});
		expect(userIsLogedIn(LOGIN_TOKEN_NAME)).toBe(true);
	});

	it("should return false whe the user session cookie is not set", () => {
		expect(userIsLogedIn(LOGIN_TOKEN_NAME)).toBe(false);
	});
});

describe("userInfoIsInCookies", () => {
	it("should call a method to get cookies", () => {
		const getCookieSpy = vi.spyOn(cookies, "getCookie");
		userInfoIsInCookies(USER_COOKIE_NAME);
		expect(getCookieSpy).toHaveBeenCalled();
	});

	it("should return true when the user session cookie is set", () => {
		vi.spyOn(cookies, "getCookie").mockImplementationOnce(() => {
			return { user: "jwt" };
		});
		expect(userInfoIsInCookies(USER_COOKIE_NAME)).toBe(true);
	});

	it("should return false whe the user session cookie is not set", () => {
		expect(userInfoIsInCookies(USER_COOKIE_NAME)).toBe(false);
	});
});
