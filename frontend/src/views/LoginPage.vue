<script setup>
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { decodeJwt } from "jose";
import { getEnvVariable } from "../utils";
import { createCookie, getCookie } from "../utils/cookies";

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

const redirectToMicrosoftLogin = () => {
	const microsoftURL = buildMicrosoftLoginURL();
	window.location.href = microsoftURL;
};

onMounted(() => {
	const router = useRouter();
	const isLoged = getCookie(LOGIN_TOKEN_NAME);

	if (isLoged) {
		router.push("/");
		return;
	}

	const params = extractParamsFromURLHash(extractHashFromURL());
	if (!params.code && !params.id_token) {
		redirectToMicrosoftLogin();
	}
	logInUser(params.id_token);
	// TODO validate state
	router.push("/");
});
</script>

<template>
	<div></div>
</template>
