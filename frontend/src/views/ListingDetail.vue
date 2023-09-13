<script setup>
import { onBeforeMount, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import {
	Dialog,
	DialogPanel,
	DialogTitle,
	DialogDescription,
} from "@headlessui/vue";
import StarsInput from "../components/Inputs/StarsInput.vue";
import ImageSelector from "../components/ImageSelector.vue";
import useModal from "../composables/useModal";
import { makeGetRequest } from "../utils/axios";

const route = useRoute();
const router = useRouter();
const listing = ref(null);
const { isModalOpen, setModalOpen } = useModal();

const handleModalClose = () => {
	router.go(-1);
};

onBeforeMount(async () => {
	const productId = route.params.id;
	const url = `${import.meta.env.VITE_API_URL}/api/listings/get/${productId}`;
	const { data } = await makeGetRequest(url);

	if (typeof data === "undefined") {
		setModalOpen(true);
	}
	if (!data) {
		router.push("/404");
	}

	listing.value = data;
});

// TODO Load dinamically
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
		<div
			class="col-span-3 lg:col-span-3"
			v-if="listing"
		>
			<h1>{{ listing.name }}</h1>
			<div class="text-lg font-semibold text-endava-600">
				${{ listing.price }}
			</div>
			<div class="flex gap-2">
				<span>
					<StarsInput
						color="#DE411B"
						:increment="0.5"
						:default-rating="listing.condition"
						:disabled="true"
					/>
				</span>
				<span>(Condition)</span>
			</div>
			<p class="my-8 pr-16 text-gray-500">
				{{ listing.detail }}
			</p>

			<p class="my-8">
				Sold by:
				<span class="font-bold">{{ listing.seller.name }}</span>
			</p>
			<button
				type="button"
				class="endava h-10 w-2/4"
			>
				Purchase
			</button>
		</div>
	</main>
	<Dialog
		:open="isModalOpen"
		@close="setModalOpen"
	>
		<div
			class="fixed inset-0 flex w-screen items-center justify-center bg-white/70 p-4"
		>
			<div class="w-30 rounded-2xl bg-white p-5">
				<DialogPanel>
					<DialogTitle>Error 500</DialogTitle>
					<DialogDescription>
						There was an error connecting with the server
					</DialogDescription>
					<button
						class="endava mt-2 border-0 px-3 py-2"
						@click="handleModalClose"
					>
						Close
					</button>
				</DialogPanel>
			</div>
		</div>
	</Dialog>
</template>
