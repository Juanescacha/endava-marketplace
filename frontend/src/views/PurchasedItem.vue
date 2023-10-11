<script setup>
	import { onBeforeMount, reactive, ref } from "vue";
	import { useRoute, useRouter } from "vue-router";
	import { useUserStore } from "@/stores/user";
	import { makeGetRequest } from "@/utils/axios";
	import { getSaleStatusColor } from "@/utils/strings";
	import StarsInput from "@/components/Inputs/StarsInput.vue";
	import ImageSelector from "@/components/Images/ImageSelector.vue";
	import GenericModal from "@/components/GenericModal.vue";
	import TeamsChatLink from "@/components/TeamsChatLink.vue";

	const route = useRoute();
	const router = useRouter();
	const purchase = ref(null);
	const isUserSure = ref(false);
	const modal = reactive({ title: "", description: null, open: false });
	const user = useUserStore();

	onBeforeMount(async () => {
		if (Number.isNaN(Number(route.params.id))) {
			router.push("/404");
			return;
		}
		await getPurchaseData();
		getListingImages();
	});

	const getPurchaseData = async () => {
		const url = `${import.meta.env.VITE_API_URL}/api/sales/get/${
			route.params.id
		}`;

		const { data } = await makeGetRequest(url);
		const isValid = validateListingFetch(data);
		if (!isValid) return;

		purchase.value = data;
	};

	const getListingImages = () => {
		const url = `${import.meta.env.VITE_API_URL}/api/listings/get/images/${
			purchase.value.listing.id
		}`;

		makeGetRequest(url).then(response => {
			const { data } = response;
			const isValid = validateListingFetch(data);
			if (!isValid) return;

			const thumbnails = [];

			const images = data.filter(img => {
				if (img.includes("thumb")) {
					thumbnails.push(img);
				}
				return !img.includes("thumb");
			});

			if (!purchase.value) purchase.value = { images };
			else purchase.value.images = data;
		});
	};

	const validateListingFetch = data => {
		if (typeof data === "undefined") {
			modal.title = "Error 500";
			modal.description = "There was an error connecting with the server";
			modal.open = true;
			return false;
		}
		if (!data) {
			router.push("/404");
			return false;
		}

		if (Array.isArray(data)) return true;

		if (!data.buyer?.id || user.id === 0) return false;

		if (data.buyer?.id !== user.id) {
			modal.title = "Error";
			modal.description = "You are not authorized to view this page";
			modal.open = true;
			return false;
		}
		return true;
	};

	const handleModalClose = () => {
		modal.title = "";
		modal.description = null;
		modal.open = false;
		isUserSure.value = false;
	};
</script>

<template>
	<generic-modal
		:title="modal.title"
		:description="modal.description"
		:open="modal.open"
		@modal-closed="handleModalClose"
	/>
	<div class="mt-6 flex w-full flex-col items-center">
		<image-selector
			v-if="purchase && purchase.images && purchase.images.length > 0"
			:images="purchase.images"
			styles="px-2 w-3/4 my-3"
		/>
		<div
			class="w-2/3"
			v-if="purchase && purchase.listing"
		>
			<h1>{{ purchase.listing.name }}</h1>
			<div class="flex gap-2">
				<span>
					<StarsInput
						color="#DE411B"
						:increment="0.5"
						:default-rating="purchase.listing.condition"
						:disabled="true"
					/>
				</span>
				<span>(Condition)</span>
			</div>
			<p class="my-6 text-gray-500">
				{{ purchase.listing.detail }}
			</p>

			<div class="flex justify-between gap-x-5">
				<div>
					<p class="text-lg font-semibold text-endava-600">
						${{ purchase.listing.price }}
					</p>
					<p>Quantity: 1</p>
					<p>
						Status:
						<span :class="getSaleStatusColor(purchase.status.name)">
							{{ purchase.status.name }}
						</span>
					</p>
				</div>

				<div>
					<p class="mt-3">
						Sold by:
						<span class="block font-bold"
							>{{ purchase.listing.seller.name }}.</span
						>
					</p>
					<p class="mb-2">
						E-mail: {{ purchase.listing.seller.email }}
					</p>
				</div>
			</div>
			<teams-chat-link
				:user="purchase.listing.seller.email"
				inner-text="Chat with the seller!"
			/>
		</div>
	</div>
</template>
