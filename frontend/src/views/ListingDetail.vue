<script setup>
import { onMounted } from "vue";
import { useRoute } from "vue-router";
import StarsInput from "../components/Inputs/StarsInput.vue";
import ImageSelector from "../components/ImageSelector.vue";
import { makeGetRequest } from "../utils/axios";

const route = useRoute();
onMounted(async () => {
	const productId = route.params.id;
	const url = `${import.meta.env.VITE_API_URL}/api/listings/get/${productId}`;
	const response = await makeGetRequest(url);
	console.log(response);
});

const images = [
	"https://cdn.pixabay.com/photo/2015/12/12/15/24/amsterdam-1089646_1280.jpg",
	"https://cdn.pixabay.com/photo/2016/02/17/23/03/usa-1206240_1280.jpg",
	"https://cdn.pixabay.com/photo/2016/12/04/19/30/berlin-cathedral-1882397_1280.jpg",
];
</script>

<template>
	<main class="mx-14 mb-12 mt-32 grid grid-cols-1 gap-x-4 lg:grid-cols-7">
		<image-selector
			:images="images"
			styles="col-span-3 lg:col-span-4"
		/>
		<div class="col-span-3 lg:col-span-3">
			<h1 class="">Product name</h1>
			<div class="text-lg font-semibold text-endava-600">$5000</div>
			<StarsInput
				color="#DE411B"
				:increment="0.5"
				:default-rating="5"
				:disabled="true"
			/>
			<p class="my-8 pr-16 text-gray-500">
				Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
				eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut
				enim ad minim veniam, quis nostrud exercitation ullamco laboris
				nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor
				in reprehenderit in voluptate velit esse cillum dolore eu fugiat
				nulla pariatur. Excepteur sint occaecat cupidatat non proident,
				sunt in culpa qui officia deserunt mollit anim id est laborum.
				Lorem ipsum dolor sit amet, consectetur adipiscing.
			</p>
			<button
				type="button"
				class="endava h-10 w-2/4"
			>
				Purchase
			</button>
		</div>
	</main>
</template>
