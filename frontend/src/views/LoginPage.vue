<script setup>
	import { onBeforeMount } from "vue";
	import { useRoute, useRouter } from "vue-router";
	import { logInUser, redirectToMicrosoftLogin } from "../utils/userSession";

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

		const params = extractParamsFromURLHash(extractHashFromURL());
		if (!params.code && !params.id_token) {
			redirectToMicrosoftLogin();
			return;
		}
		logInUser(params.id_token);
		// TODO validate state
		router.push("/");
	});
</script>

<template>
	<div class="flex h-screen w-screen items-center justify-center">
		<div
			class="inline-block h-6 w-6 animate-spin rounded-full border-[3px] border-current border-t-transparent text-orange-600"
			role="status"
			aria-label="loading"
		>
			<span class="sr-only">Loading...</span>
		</div>
	</div>
</template>
