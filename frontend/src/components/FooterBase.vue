<script setup>
	import GoogleIcon from "../assets/icons/GoogleIcon.vue";
	import GithubIcon from "../assets/icons/GithubIcon.vue";
	import TwitterIcon from "../assets/icons/TwitterIcon.vue";
	import SlackIcon from "../assets/icons/SlackIcon.vue";
</script>

<template>
	<footer class="mx-auto mt-auto w-full px-4 py-5 sm:px-6 lg:px-8">
		<div class="text-center">
			<div class="mt-3">
				<p class="text-gray-500">We're part of the Endava Family.</p>
				<p class="text-gray-500">
					Endava Marketplace © All Rights Reserved. 2023.
				</p>
			</div>
			<div class="mt-3 space-x-2">
				<a
					class="inline-flex h-10 w-10 items-center justify-center rounded-full text-center text-gray-500 transition hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:ring-offset-2 focus:ring-offset-white dark:text-gray-500 dark:hover:bg-gray-800 dark:hover:text-gray-200"
					href="https://www.google.com/"
					target="_blank"
					aria-label="Link to Google"
				>
					<GoogleIcon
						class="h-3.5 w-3.5"
						fill="currentColor"
					/>
				</a>
				<a
					class="inline-flex h-10 w-10 items-center justify-center rounded-full text-center text-gray-500 transition hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:ring-offset-2 focus:ring-offset-white dark:text-gray-500 dark:hover:bg-gray-800 dark:hover:text-gray-200"
					href="https://twitter.com/endavalatam"
					target="_blank"
					aria-label="Link to endavalatam Twitter/X"
				>
					<TwitterIcon
						class="h-3.5 w-3.5"
						fill="currentColor"
					/>
				</a>
				<a
					class="inline-flex h-10 w-10 items-center justify-center rounded-full text-center text-gray-500 transition hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:ring-offset-2 focus:ring-offset-white dark:text-gray-500 dark:hover:bg-gray-800 dark:hover:text-gray-200"
					href="https://www.github.com/endava"
					target="_blank"
					aria-label="Link to endava's Github"
				>
					<GithubIcon
						class="h-3.5 w-3.5"
						fill="currentColor"
					/>
				</a>
				<a
					class="inline-flex h-10 w-10 items-center justify-center rounded-full text-center text-gray-500 transition hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:ring-offset-2 focus:ring-offset-white dark:text-gray-500 dark:hover:bg-gray-800 dark:hover:text-gray-200"
					href="https://www.slack.com/"
					target="_blank"
					aria-label="Link to Slack's website"
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
