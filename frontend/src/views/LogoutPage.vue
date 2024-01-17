<script setup>
	import {
		CheckCircleIcon,
		ArrowRightOnRectangleIcon,
	} from "@heroicons/vue/24/solid";
</script>
<template>
	<div class="mx-auto flex h-screen w-full max-w-[50rem] flex-col">
		<header class="z-50 mb-auto flex w-full justify-center py-4">
			<nav
				class="px-4 sm:px-6 lg:px-8"
				aria-label="Global"
			>
				<router-link
					class="flex-none text-xl font-semibold"
					to="/"
					aria-label="Brand"
					><img
						src="../assets/endava-logo.png"
						alt="logo"
						class="w-32"
				/></router-link>
			</nav>
		</header>

		<div class="px-4 py-10 text-center sm:px-6 lg:px-8">
			<CheckCircleIcon class="mx-auto w-32 text-green-400" />
			<h1 class="block text-2xl font-bold text-white"></h1>
			<p class="mt-3 font-bold text-gray-800 dark:text-gray-400">
				You have been logged out
			</p>
			<p class="text-gray-600 dark:text-gray-400">Thank You.</p>
			<div
				class="mt-5 flex flex-col items-center justify-center gap-2 sm:flex-row sm:gap-3"
			>
				<router-link
					class="endava flex w-full items-center justify-center gap-x-3 rounded-md border border-transparent px-4 py-3 text-center text-sm font-medium text-white transition focus:outline-none focus:ring-2 focus:ring-endava-600 focus:ring-offset-2 focus:ring-offset-white sm:w-auto"
					to="/login"
					><ArrowRightOnRectangleIcon class="w-5" />
					Log in
				</router-link>
			</div>
		</div>

		<footer class="mt-auto py-5 text-center">
			<div class="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
				<p class="text-sm text-gray-500">
					Endava Marketplace © All Rights Reserved. 2023.
				</p>
			</div>
		</footer>
	</div>
</template>
