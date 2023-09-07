<script setup>
import { onBeforeMount, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { decodeJwt } from "jose";
import { createCookie } from "../utils/cookies";

// TODO remove temporary state
const state = ref(`${Math.random() * 100}`);
const LOGIN_TOKEN_NAME = "access_token";

const extractHashFromURL = () => {
	const route = useRoute();
	return route.hash;
};

const extractParamsFromURLHash = hash => {
	const params = {};
	let tempHash = hash;
	if (hash.charAt(0) === "#") {
		tempHash = hash.slice(1);
	}

	const urlParams = tempHash.split("&");
	urlParams.forEach(param => {
		const [key, value] = param.split("=");
		params[key] = value;
	});

	return params;
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

const redirectToMicrosoftLogin = () => {
	const microsoftURL = buildMicrosoftLoginURL();
	window.location.href = microsoftURL;
};

onBeforeMount(() => {
	const router = useRouter();

	const params = extractParamsFromURLHash(extractHashFromURL());
	if (!params.code && !params.id_token) {
		redirectToMicrosoftLogin();
	}
	logInUser(params.id_token);
	// TODO validate state
	router.push("/");
});
</script>
