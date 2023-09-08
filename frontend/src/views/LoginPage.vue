<script setup>
import { onBeforeMount } from "vue";
import { useRoute, useRouter } from "vue-router";
import {
	logInUser,
	redirectToMicrosoftLogin,
	userIsLogedIn,
} from "../utils/userSession";

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

onBeforeMount(() => {
	const router = useRouter();

	if (userIsLogedIn()) {
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
