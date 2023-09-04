<template>
	<main
		id="content"
		role="main"
		class="mt-28"
	>
		<div class="mx-auto my-5 flex max-w-6xl flex-wrap justify-center gap-6">
			<ProductCard
				v-for="card in cardsInfo"
				:cardInfo="card"
				:key="card.id"
			/>
		</div>
	</main>
	<nav class="flex items-center justify-center space-x-2">
		<a
			class="inline-flex items-center gap-2 rounded-md p-4 text-gray-500 hover:text-endava-400"
			href="#"
		>
			<span aria-hidden="true">
				<ChevronDoubleLeftIcon class="h-5 w-5" />
			</span>
			<span class="sr-only">Previous</span>
		</a>
		<a
			class="inline-flex h-10 w-10 items-center rounded-full bg-endava-400 p-4 text-sm font-medium text-white"
			href="#"
			aria-current="page"
			>1</a
		>
		<a
			class="inline-flex h-10 w-10 items-center rounded-full p-4 text-sm font-medium text-gray-500 hover:text-endava-400"
			href="#"
			>2</a
		>
		<a
			class="inline-flex h-10 w-10 items-center rounded-full p-4 text-sm font-medium text-gray-500 hover:text-endava-400"
			href="#"
			>3</a
		>
		<a
			class="inline-flex items-center gap-2 rounded-md p-4 text-gray-500 hover:text-endava-400"
			href="#"
		>
			<span class="sr-only">Next</span>
			<span aria-hidden="true">
				<ChevronDoubleRightIcon class="h-5 w-5" />
			</span>
		</a>
	</nav>
</template>
<script setup>
import ProductCard from "../components/ProductCard.vue";
import { cardsInfo } from "../constants";
import {
	ChevronDoubleRightIcon,
	ChevronDoubleLeftIcon,
} from "@heroicons/vue/20/solid";
</script>
