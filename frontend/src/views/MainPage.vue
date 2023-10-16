<script setup>
	import { ref, onMounted } from "vue";
	import { makeGetRequest } from "../utils/axios";
	import ProductCard from "../components/ProductCard.vue";
	import SkeletonCard from "../components/SkeletonCard.vue";
	import { useProductStore } from "../stores/products";
	const productsList = useProductStore();

	const isLoading = ref(true);
	const page = ref(1);

	let Baseurl = `${import.meta.env.VITE_API_URL}/api/listings/search/get`;

	onMounted(async () => {
		const url = `${Baseurl}`;
		const response = await makeGetRequest(url);
		if (response.error) {
			// error
		} else {
			// ***
			// productCards.value = response.data.content;
			productsList.update(response.data.content);
			setTimeout(() => {
				isLoading.value = false;
			}, 500);
		}
	});

	const handleLoadMore = async () => {
		page.value += 1;
		const url = `${Baseurl}${page.value}`;
		const response = await makeGetRequest(url);

		// productCards.value = [...productCards.value, ...response.data.content];
		productsList.update([
			...productsList.products,
			...response.data.content,
		]);
	};
</script>

<template>
	<main
		id="content"
		role="main"
		class="mx-auto mt-28 w-fit"
	>
		<h1 class="py-2">Latest Products</h1>

		<div
			:class="isLoading ? 'hidden' : 'block'"
			class="mx-auto my-5 flex max-w-6xl flex-wrap justify-center gap-6"
		>
			<!-- ** -->
			<!-- EN EL v-for es "card in productCards" -->
			<ProductCard
				v-for="card in productsList.productCards"
				:cardInfo="card"
				:key="card.id"
			/>
		</div>
		<div
			:class="isLoading ? 'block' : 'hidden'"
			class="mx-auto my-5 flex max-w-6xl flex-wrap justify-center gap-6"
		>
			<SkeletonCard
				v-for="index in 10"
				:key="index"
			/>
		</div>
	</main>
	<button
		type="button"
		@click="handleLoadMore"
		class="endava mx-auto block items-center justify-center gap-2 rounded-md border px-4 py-3 text-sm font-semibold text-white shadow-sm transition-all"
	>
		Load More
	</button>
</template>
