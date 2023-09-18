<script setup>
	import { ref } from "vue";
	import ProductCard from "../components/ProductCard.vue";
	import SkeletonCard from "../components/SkeletonCard.vue";
	import { cardsInfo } from "../constants";
	import {
		ChevronDoubleRightIcon,
		ChevronDoubleLeftIcon,
	} from "@heroicons/vue/20/solid";

	const productCards = ref(cardsInfo);
	/* funcion () {
	request === fetch('{{URL}}/api/listings/get/all')
	productCards = ref(request.content)
	} */
</script>

<template>
	<main
		id="content"
		role="main"
		class="mx-auto mt-28 w-fit"
	>
		<h1 class="py-2">Latest Products</h1>
		<div
			v-if="productCards.length === 0"
			class="mx-auto my-5 flex max-w-6xl flex-wrap justify-center gap-6"
		>
			<SkeletonCard
				v-for="index in 20"
				:key="index"
			/>
		</div>
		<div class="mx-auto my-5 flex max-w-6xl flex-wrap justify-center gap-6">
			<ProductCard
				v-for="card in productCards"
				:cardInfo="card"
				:key="card.id"
			/>
		</div>
	</main>
	<button
		type="button"
		class="endava mx-auto block items-center justify-center gap-2 rounded-md border px-4 py-3 text-sm font-semibold text-white shadow-sm transition-all"
	>
		Load More
	</button>
</template>
