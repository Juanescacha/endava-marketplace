<script setup>
	import { onBeforeMount, ref } from "vue";
	import { MenuItem } from "@headlessui/vue";
	import {
		CheckIcon,
		XMarkIcon,
		EllipsisHorizontalCircleIcon,
	} from "@heroicons/vue/24/outline";
	import { useUserStore } from "../stores/user";
	import { getSellerSales } from "../utils/axios";
	import { trimTextToLength, extractFirstWordsFromText } from "../utils";
	import ProductListItem from "../components/Menus/ProductListItem.vue";
	import DropdownMenu from "../components/Menus/DropdownMenu.vue";

	const user = useUserStore();
	const soldProducts = ref([]);

	onBeforeMount(async () => {
		const { data, error } = await getSellerSales(user.id);

		if (error) return;
		soldProducts.value = data;
	});

	const getSaleStatusColor = status => {
		switch (status) {
			case "Pending":
				return "text-orange-400";

			case "Fulfilled":
				return "text-green-400";

			case "Canceled":
				return "text-red-400";
			default:
				break;
		}
	};
</script>
<template>
	<div class="flex w-full flex-col items-center overflow-y-auto lg:mx-8">
		<div class="sticky top-0 my-6 w-full bg-white py-2 text-center">
			<h1>Sold products</h1>
		</div>
		<ul
			v-if="soldProducts.length > 0"
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
							:src="'https://ps.w.org/replace-broken-images/assets/icon-256x256.png?rev=2561727'"
							:alt="`${extractFirstWordsFromText(
								product.listing.name,
								2
							)} thumbnail`"
							class="h-auto w-12 rounded-xl"
						/>
						<h2
							:title="product.listing.name"
							class="text-lg"
						>
							{{ trimTextToLength(product.listing.name, 15) }}
						</h2>
					</template>
					<template v-slot:right-side>
						<div class="grid grid-cols-2 gap-x-4 gap-y-2">
							<span :title="product.buyer.name">
								Buyer:
								<span class="font-bold">{{
									extractFirstWordsFromText(
										product.buyer.name,
										2
									)
								}}</span></span
							>
							<span>Date: {{ product.date }}</span>
							<span>Qty: 1</span>
							<span
								:class="getSaleStatusColor(product.status.name)"
								>{{ product.status.name }}</span
							>
						</div>

						<DropdownMenu v-if="product.status.name === 'Pending'">
							<template v-slot:menu-button>
								<EllipsisHorizontalCircleIcon
									class="h-8 w-8 text-endava-500"
								/>
							</template>
							<template v-slot:menu-items>
								<MenuItem>
									<button
										class="flex w-full items-center gap-x-3.5 rounded-md px-3 py-2 text-sm text-gray-800 hover:bg-gray-100 active:bg-gray-200"
									>
										<CheckIcon class="h-5 w-5 flex-none" />
										Complete sale
									</button>
								</MenuItem>
								<MenuItem>
									<button
										class="flex w-full items-center gap-x-3.5 rounded-md px-3 py-2 text-sm text-gray-800 hover:bg-gray-100 active:bg-gray-200"
									>
										<XMarkIcon class="h-5 w-5 flex-none" />
										Cancel sale
									</button>
								</MenuItem>
							</template>
						</DropdownMenu>
					</template>
				</product-list-item>
			</li>
		</ul>
		<div v-else>Nothing here yet. 😕</div>
	</div>
</template>
