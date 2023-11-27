<script setup>
	import { ref, onMounted } from "vue";
	import { getListingsSearch } from "@/utils/axios";
	import ProductCard from "@/components/ProductCard.vue";
	import SkeletonCard from "@/components/SkeletonCard.vue";
	import { useProductStore } from "@/stores/products";
	const productsList = useProductStore();

	const isLoading = ref(true);
	const page = ref(1);
	const totalPages = ref(1);
	const noMoreProducts = ref(false);

	let Baseurl = `${import.meta.env.VITE_API_URL}/api/listings/search/get`;

	onMounted(async () => {
		const response = await getListingsSearch();
		if (response.error) {
			// error
		} else {
			// ***
			// productCards.value = response.data.content;
			productsList.update(response.data.content);
			totalPages.value = response.data.totalPages;
			setTimeout(() => {
				isLoading.value = false;
			}, 250);
		}
	});

	const handleLoadMore = async () => {
		if (page.value < totalPages.value) {
			page.value += 1;
			const response = await getListingsSearch({ page: page.value });

			productsList.update([
				...productsList.productCards,
				...response.data.content,
			]);
		} else {
			noMoreProducts.value = true;
		}
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
		v-if="!noMoreProducts"
		type="button"
		@click="handleLoadMore"
		class="endava mx-auto block items-center justify-center gap-2 rounded-md border px-4 py-3 text-sm font-semibold text-white shadow-sm transition-all"
	>
		Load More
	</button>
	<div
		v-else
		class="mt-10 text-center italic text-gray-500"
	>
		No more items...
	</div>
</template>
