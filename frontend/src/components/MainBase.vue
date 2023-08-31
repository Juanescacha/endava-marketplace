<template>
	<main
		id="content"
		role="main"
		class="mt-28"
	>
		<!-- Nav -->
		<!-- <nav
			class="relative -top-px z-0 mt-[79px] border-t bg-white pt-6 text-sm font-medium text-black shadow-sm shadow-gray-100 ring-1 ring-gray-900 ring-opacity-5 dark:border-gray-800 dark:bg-slate-900 dark:shadow-slate-700/[.7] md:pb-6"
			aria-label="Jump links"
		>
			<div
				class="scrollbar-x dark:scrollbar-x mx-auto flex w-full max-w-7xl snap-x items-center overflow-x-auto px-4 pb-4 sm:px-6 md:pb-0 lg:px-8"
			>
				<div class="sm:last-pr-0 shrink-0 snap-center pr-5 sm:pr-8">
					<a
						class="inline-flex items-center gap-x-2 hover:text-endava-500 dark:text-gray-400 dark:hover:text-gray-500"
						href="#"
						>Dashboard</a
					>
				</div>
				<div class="shrink-0 snap-center pr-5 sm:pr-8 sm:last:pr-0">
					<a
						class="inline-flex items-center gap-x-2 hover:text-endava-500 dark:text-gray-400 dark:hover:text-gray-500"
						href="#"
						>Users</a
					>
				</div>
				<div class="shrink-0 snap-center pr-5 sm:pr-8 sm:last:pr-0">
					<a
						class="inline-flex items-center gap-x-2 hover:text-endava-500 dark:text-gray-400 dark:hover:text-gray-500"
						href="#"
						>Account</a
					>
				</div>
				<div class="shrink-0 snap-center pr-5 sm:pr-8 sm:last:pr-0">
					<a
						class="inline-flex items-center gap-x-2 hover:text-endava-500 dark:text-gray-400 dark:hover:text-gray-500"
						href="#"
						>Projects</a
					>
				</div>
				<div class="shrink-0 snap-center pr-5 sm:pr-8 sm:last:pr-0">
					<a
						class="inline-flex items-center gap-x-2 hover:text-endava-500 dark:text-gray-400 dark:hover:text-gray-500"
						href="#"
						>Calendar</a
					>
				</div>
				<div class="shrink-0 snap-center pr-5 sm:pr-8 sm:last:pr-0">
					<a
						class="inline-flex items-center gap-x-2 hover:text-endava-500 dark:text-gray-400 dark:hover:text-gray-500"
						href="#"
					>
						Documentation
					</a>
				</div>
			</div>
		</nav> -->
		<!-- End Nav -->

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
			<span aria-hidden="true">«</span>
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
			<span aria-hidden="true">»</span>
		</a>
	</nav>
</template>
<script setup>
import ProductCard from "../components/ProductCard.vue";
import { cardsInfo } from "../constants";
</script>
