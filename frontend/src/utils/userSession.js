import { decodeJwt } from "jose";
import { createCookie, getCookie } from "../utils/cookies";

const LOGIN_TOKEN_NAME = "access_token";
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
	const microsoftURL = buildMicrosoftLoginURL();
	window.location.href = microsoftURL;
};

const logInUser = token => {
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

const userIsLogedIn = () => !!getCookie(LOGIN_TOKEN_NAME);

export { logInUser, redirectToMicrosoftLogin, userIsLogedIn };
