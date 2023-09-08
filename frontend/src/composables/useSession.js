import { ref } from "vue";
import { decodeJwt } from "jose";
import { getEnvVariable } from "../utils";
import { getCookie, createCookie } from "../utils/cookies";

const LOGIN_TOKEN_NAME = "access_token";

export default function useSession() {
	const state = ref(`${Math.random() * 100}`); // TODO remove temporary state

	const redirectToMicrosoftLogin = () => {
		const microsoftURL = buildMicrosoftLoginURL();
		window.location.href = microsoftURL;
	};

	const buildMicrosoftLoginURL = () => {
		const microsoftURL = getEnvVariable("VITE_MICROSOFT_LOGIN_URL");
		const tenantId = `/${getEnvVariable("VITE_TENANT_ID")}`;
		const microsoftURL2 = `/${getEnvVariable(
			"VITE_MICROSOFT_LOGIN_URL_END"
		)}/authorize`;
		const clientId = `?client_id=${getEnvVariable("VITE_CLIENT_ID")}`;
		const redirectURI = `&redirect_uri=${getEnvVariable(
			"VITE_URL_REDIRECT_URI"
		)}`;
		const otherParams = getEnvVariable("VITE_MICROSOFT_LOGIN_PARAMS");

		const stateParam = `&state=${state.value}`;
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

	return { logInUser, redirectToMicrosoftLogin, userIsLogedIn };
}
