<script setup>
	import { ref, onMounted } from "vue";
	import { getListingsSearch } from "@/utils/axios";
	import ProductCard from "@/components/ProductCard.vue";
	import SkeletonCard from "@/components/SkeletonCard.vue";
	import { useProductStore } from "@/stores/products";
	import { useProductsSearchStore } from "@/stores/productsSearch";
	const productsList = useProductStore();
	const productsSearch = useProductsSearchStore();

	const isLoading = ref(true);
	const page = ref(1);
	const totalPages = ref(2);

	onMounted(async () => {
		const response = await getListingsSearch();
		if (response.error) {
			// error
		} else {
			productsList.update(response.data.content);
			totalPages.value = response.data.totalPages;
			setTimeout(() => {
				isLoading.value = false;
			}, 250);
		}
	});

	const handleLoadMore = async () => {
		page.value += 1;
		let params = {};

		if (productsSearch.search.value) {
			params.name = productsSearch.search.value;
		}
		if (productsSearch.categoryId.value) {
			params.category = productsSearch.categoryId.value;
		}
		params.page = page.value;

		const response = await getListingsSearch(params);

		productsList.update([
			...productsList.productCards,
			...response.data.content,
		]);
	};
</script>

<template>
	<main
		id="content"
		role="main"
		class="mx-auto mt-8 w-fit"
	>
		<h1 class="py-2">Latest Products</h1>

		<div
			:class="isLoading ? 'hidden' : 'block'"
			class="mx-auto my-5 flex max-w-6xl flex-wrap justify-center gap-6"
		>
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
		v-if="page < totalPages && !isLoading"
		type="button"
		@click="handleLoadMore"
		class="endava mx-auto block items-center justify-center gap-2 rounded-md border px-4 py-3 text-sm font-semibold text-white shadow-sm transition-all focus:outline-blue-500"
	>
		Load More
	</button>
</template>
