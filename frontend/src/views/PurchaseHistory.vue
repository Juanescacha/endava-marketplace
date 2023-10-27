<script setup>
	import { onBeforeMount, ref, watch } from "vue";
	import { RouterLink } from "vue-router";
	import { useUserStore } from "@/stores/user";
	import { getUserPurchases } from "@/utils/axios";
	import {
		extractFirstWordsFromText,
		getSaleStatusColor,
	} from "@/utils/strings";
	import ProductListItem from "@/components/Menus/ProductListItem.vue";

	const user = useUserStore();
	const boughtProducts = ref([]);

	onBeforeMount(async () => await getPageData());

	watch(user, async () => await getPageData());

	const getPageData = async () => {
		const { data } = await getUserPurchases(user.id);
		if (data) boughtProducts.value = data;
	};
</script>

<template>
	<div class="flex w-full flex-col items-center overflow-y-auto lg:mx-8">
		<div class="sticky top-0 my-6 w-full bg-white py-2 text-center">
			<h1>Bought products</h1>
		</div>
		<ul
			v-if="boughtProducts.length > 0"
			class="flex w-full flex-col items-center gap-4 px-2"
		>
			<li
				v-for="product in boughtProducts"
				:key="product.id"
				class="w-full max-w-[1100px] rounded-2xl bg-gray-100"
			>
				<router-link :to="`purchase/${product.id}`">
					<product-list-item>
						<template v-slot:left-side>
							<img
								src="https://ps.w.org/replace-broken-images/assets/icon-256x256.png?rev=2561727"
								:alt="`${extractFirstWordsFromText(
									product.listing.name,
									2
								)} thumbnail`"
								class="h-auto w-12 rounded-xl"
							/>
							<h2
								:title="product.listing.name"
								class="line-clamp-2 max-w-[70%] text-ellipsis text-lg"
							>
								{{ product.listing.name }}
							</h2>
						</template>
						<template v-slot:right-side>
							<div class="grid grid-cols-2 gap-x-4 gap-y-2">
								<p :title="product.listing.seller.name">
									<span class="hidden sm:inline">
										Seller:
									</span>
									<span class="font-bold">
										{{
											extractFirstWordsFromText(
												product.listing.seller.name,
												2
											)
										}}
									</span>
								</p>
								<p>Qty: 1</p>
								<p>
									<span class="hidden sm:inline">
										Date:
									</span>
									{{ product.date }}
								</p>
								<p
									:class="
										getSaleStatusColor(product.status.name)
									"
								>
									{{ product.status.name }}
								</p>
							</div>
							<h3>${{ product.listing.price }}</h3>
						</template>
					</product-list-item>
				</router-link>
			</li>
		</ul>
		<p v-else>Nothing here yet. 😕</p>
	</div>
</template>
