import { decodeJwt } from "jose";
import { useUserStore } from "@/stores/user";
import { createCookie, getCookie, deleteCookie } from "@/utils/cookies";
import { postUser } from "@/utils/axios";

const LOGIN_TOKEN_NAME = "access_token";
const USER_COOKIE_NAME = "user";
// TODO remove temporary state
const state = `${Math.random() * 1000}`;

const buildMicrosoftLoginURL = () => {
	const microsoftURL = import.meta.env.VITE_MICROSOFT_LOGIN_URL;
	const tenantId = `/${import.meta.env.VITE_TENANT_ID}`;
	const microsoftURL2 = `/${
		import.meta.env.VITE_MICROSOFT_LOGIN_URL_END
	}/authorize`;
	const clientId = `?client_id=${import.meta.env.VITE_CLIENT_ID}`;
	const redirectURI = `&redirect_uri=${
		import.meta.env.VITE_URL_REDIRECT_URI
	}`;
	const otherParams = import.meta.env.VITE_MICROSOFT_LOGIN_PARAMS;

	const stateParam = `&state=${state}`;
	const nonce = `&nonce=12345`;

	return (
		microsoftURL +
		tenantId +
		microsoftURL2 +
		clientId +
		redirectURI +
		otherParams +
		stateParam +
		nonce
	);
};

const redirectToMicrosoftLogin = () => {
	window.location.href = buildMicrosoftLoginURL();
};

const logInUser = token => {
	createSessionCookie(token);
	saveUserInfoFromServerToStore();
};

const createSessionCookie = token => {
	const { iat, exp } = decodeJwt(token);
	const tokenLifeSeconds = exp - iat;
	const currentDate = new Date();
	currentDate.setSeconds(currentDate.getSeconds() + tokenLifeSeconds);
	createCookie({
		key: LOGIN_TOKEN_NAME,
		value: token,
		expiration: currentDate.toUTCString(),
	});
};

const saveUserInfoFromServerToStore = async () => {
	const user = useUserStore();
	const response = await postUser();

	if (!response?.id) {
		return;
	}
	user.id = response.id;
	user.name = response.name;
	user.email = response.email;
	user.isAdmin = response.admin;
};

const saveUserInfoFromStoreToCookies = () => {
	const user = useUserStore();
	if (user.id === 0) return;

	const { id, name, email, isAdmin } = user;
	const currentDate = new Date();
	currentDate.setSeconds(currentDate.getSeconds() + 15);

	createCookie({
		key: USER_COOKIE_NAME,
		value: JSON.stringify({
			id,
			name,
			email,
			isAdmin,
		}),
		expiration: currentDate.toUTCString(),
	});
};

const saveUserInfoFromCookiesToStore = () => {
	const user = useUserStore();
	const userCookie = getCookie(USER_COOKIE_NAME);
	if (userCookie) {
		const userObj = JSON.parse(userCookie);
		user.id = userObj.id;
		user.name = userObj.name;
		user.email = userObj.email;
		user.isAdmin = userObj.isAdmin;
	}
};

const userIsLogedIn = () => !!getCookie(LOGIN_TOKEN_NAME);

const userInfoIsInCookies = () => !!getCookie(USER_COOKIE_NAME);

const logoutUser = () => {
	const user = useUserStore();
	user.$reset();
	deleteCookie(LOGIN_TOKEN_NAME);
	window.location.reload();
};

export {
	logInUser,
	redirectToMicrosoftLogin,
	saveUserInfoFromServerToStore,
	logoutUser,
	saveUserInfoFromStoreToCookies,
	saveUserInfoFromCookiesToStore,
	userInfoIsInCookies,
	userIsLogedIn,
};
