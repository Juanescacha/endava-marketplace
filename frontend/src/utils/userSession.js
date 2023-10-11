import { decodeJwt } from "jose";
import { useUserStore } from "../stores/user";
import { createCookie, getCookie } from "../utils/cookies";
import { postUser } from "./axios";

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
	saveUserInfoToStore();
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

const saveUserInfoToStore = async () => {
	const user = useUserStore();
	const { id, name, email, admin } = await postUser();
	user.$patch({
		id,
		name,
		email,
		isAdmin: admin,
	});
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
	const userInfo = JSON.parse(getCookie(USER_COOKIE_NAME));
	user.$patch(userInfo);
};

const userIsLogedIn = () => !!getCookie(LOGIN_TOKEN_NAME);

const userInfoIsInCookies = () => !!getCookie(USER_COOKIE_NAME);

export {
	logInUser,
	redirectToMicrosoftLogin,
	saveUserInfoToStore,
	saveUserInfoFromStoreToCookies,
	saveUserInfoFromCookiesToStore,
	userInfoIsInCookies,
	userIsLogedIn,
};
