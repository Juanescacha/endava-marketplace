<script setup>
	import { onBeforeMount, reactive, ref, watch } from "vue";
	import { MenuItem } from "@headlessui/vue";
	import {
		CheckIcon,
		XMarkIcon,
		EllipsisHorizontalCircleIcon,
	} from "@heroicons/vue/24/outline";
	import { useUserStore } from "@/stores/user";
	import { getSellerSales, patchSaleStatus } from "@/utils/axios";
	import {
		extractFirstWordsFromText,
		getSaleStatusColor,
	} from "@/utils/strings";
	import ProductListItem from "@/components/Menus/ProductListItem.vue";
	import DropdownMenu from "@/components/Menus/DropdownMenu.vue";
	import BasicSpinner from "@/components/BasicSpinner.vue";
	import GenericModal from "@/components/GenericModal.vue";
	import missingImage from "@/assets/no-image.png";

	const user = useUserStore();
	const itemsBeingProcessed = ref([]);
	const soldProducts = ref([]);
	const modal = reactive({ open: false });
	const update = reactive({ intention: "", onProduct: 0 });

	onBeforeMount(async () => await getPageData());

	watch(user, async () => await getPageData());

	const getPageData = async () => {
		const { data } = await getSellerSales(user.id);
		if (data && Array.isArray(data)) soldProducts.value = data;
	};

	const handleSaleStatusUpdate = (product, intention) => {
		modal.open = true;
		update.onProduct = product;
		update.intention = intention;
	};

	const handleModalClose = () => {
		modal.open = false;
		update.intention = "";
		update.onProduct = 0;
	};

	const updateSaleStatus = async () => {
		itemsBeingProcessed.value.push(update.onProduct);
		const { error } = await patchSaleStatus(
			update.onProduct,
			update.intention
		);

		if (!error) {
			getPageData();
		}

		const i = itemsBeingProcessed.value.indexOf(update.onProduct);
		itemsBeingProcessed.value.splice(i, 1);
		handleModalClose();
	};
</script>

<template>
	<div class="flex w-full flex-col items-center overflow-y-auto lg:mx-8">
		<div class="sticky top-0 my-6 w-full bg-white py-2 text-center">
			<h1>Sold products</h1>
		</div>
		<ul
			v-if="soldProducts && soldProducts.length > 0"
			class="flex w-full flex-col items-center gap-4 px-2"
		>
			<li
				v-for="product in soldProducts"
				:key="product.id"
				class="w-full max-w-[1100px] rounded-2xl bg-gray-100"
			>
				<product-list-item>
					<template v-slot:left-side>
						<img
							:src="product.listing_thumbnail || missingImage"
							:alt="`${extractFirstWordsFromText(
								product.listing_name,
								2
							)} thumbnail`"
							class="h-12 w-12 rounded-xl object-cover"
						/>
						<h2
							:title="product.listing_name"
							class="line-clamp-2 max-w-[70%] text-ellipsis text-lg"
						>
							{{ product.listing_name }}
						</h2>
					</template>
					<template v-slot:right-side>
						<div class="grid grid-cols-2 gap-x-4 gap-y-2">
							<p :title="product.buyer_name">
								<span class="hidden sm:inline">Buyer: </span>
								<span class="font-bold">{{
									extractFirstWordsFromText(
										product.buyer_name,
										2
									)
								}}</span>
							</p>
							<p>
								<span class="hidden sm:inline">Date: </span>
								{{ product.date }}
							</p>
							<p>Qty: {{ product.quantity || 1 }}</p>
							<p :class="getSaleStatusColor(product.status)">
								{{ product.status }}
							</p>
						</div>

						<basic-spinner
							v-if="itemsBeingProcessed.includes(product.id)"
						></basic-spinner>
						<dropdown-menu v-else-if="product.status === 'Pending'">
							<template v-slot:menu-button>
								<EllipsisHorizontalCircleIcon
									class="h-8 w-8 text-endava-500"
								/>
							</template>
							<template v-slot:menu-items>
								<MenuItem>
									<button
										class="flex w-full items-center gap-x-3.5 rounded-md px-3 py-2 text-sm text-gray-800 hover:bg-gray-100 active:bg-gray-200"
										@click="
											handleSaleStatusUpdate(
												product.id,
												'fulfill'
											)
										"
									>
										<CheckIcon class="h-5 w-5 flex-none" />
										Complete sale
									</button>
								</MenuItem>
								<MenuItem>
									<button
										class="flex w-full items-center gap-x-3.5 rounded-md px-3 py-2 text-sm text-gray-800 hover:bg-gray-100 active:bg-gray-200"
										@click="
											handleSaleStatusUpdate(
												product.id,
												'cancel'
											)
										"
									>
										<XMarkIcon class="h-5 w-5 flex-none" />
										Cancel sale
									</button>
								</MenuItem>
							</template>
						</dropdown-menu>
					</template>
				</product-list-item>
			</li>
		</ul>
		<p v-else>Nothing here yet. 😕</p>
	</div>
	<generic-modal
		title=""
		:open="modal.open"
		@modal-closed="handleModalClose"
	>
		<h2>Are you sure?</h2>
		<div class="my-3 flex gap-4">
			<button
				class="endava h-10 w-20 px-5 py-2"
				@click="updateSaleStatus"
			>
				Yes!
			</button>
			<button
				class="endava-gray h-10 w-20 px-5 py-2"
				@click="handleModalClose"
			>
				No
			</button>
		</div>
	</generic-modal>
</template>
