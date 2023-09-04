<template>
	<footer
		class="bottom-0 mx-auto mt-auto w-full max-w-[85rem] px-4 py-5 sm:px-6 lg:px-8"
	>
		<div class="text-center">
			<div>
				<a
					class="flex-none text-xl font-semibold text-gray-800 dark:text-white"
					href="#"
					aria-label="Brand"
					>Endava Marketplace</a
				>
			</div>

			<div class="mt-3">
				<p class="text-gray-500">We're part of the endava family.</p>
				<p class="text-gray-500">
					© Marketplace. 2023 Endava. All rights reserved.
				</p>
			</div>

			<div class="mt-3 space-x-2">
				<a
					class="inline-flex h-10 w-10 items-center justify-center rounded-full text-center text-gray-500 transition hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:ring-offset-2 focus:ring-offset-white dark:text-gray-500 dark:hover:bg-gray-800 dark:hover:text-gray-200"
					href="#"
				>
					<GoogleIcon
						class="h-3.5 w-3.5"
						fill="currentColor"
					/>
				</a>
				<a
					class="inline-flex h-10 w-10 items-center justify-center rounded-full text-center text-gray-500 transition hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:ring-offset-2 focus:ring-offset-white dark:text-gray-500 dark:hover:bg-gray-800 dark:hover:text-gray-200"
					href="#"
				>
					<TwitterIcon
						class="h-3.5 w-3.5"
						fill="currentColor"
					/>
				</a>
				<a
					class="inline-flex h-10 w-10 items-center justify-center rounded-full text-center text-gray-500 transition hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:ring-offset-2 focus:ring-offset-white dark:text-gray-500 dark:hover:bg-gray-800 dark:hover:text-gray-200"
					href="#"
				>
					<GithubIcon
						class="h-3.5 w-3.5"
						fill="currentColor"
					/>
				</a>
				<a
					class="inline-flex h-10 w-10 items-center justify-center rounded-full text-center text-gray-500 transition hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:ring-offset-2 focus:ring-offset-white dark:text-gray-500 dark:hover:bg-gray-800 dark:hover:text-gray-200"
					href="#"
				>
					<SlackIcon
						class="h-3.5 w-3.5"
						fill="currentColor"
					/>
				</a>
			</div>
		</div>
	</footer>
</template>
<script setup>
import GoogleIcon from "../assets/icons/GoogleIcon.vue";
import GithubIcon from "../assets/icons/GithubIcon.vue";
import TwitterIcon from "../assets/icons/TwitterIcon.vue";
import SlackIcon from "../assets/icons/SlackIcon.vue";
</script>
