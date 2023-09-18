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
			<h1
				class="block font-sans text-7xl font-bold text-gray-800 dark:text-white sm:text-9xl"
			>
				404
			</h1>
			<h1 class="block text-2xl font-bold text-white"></h1>
			<p class="mt-3 text-gray-600 dark:text-gray-400">
				Oops, something went wrong.
			</p>
			<p class="text-gray-600 dark:text-gray-400">
				Sorry, we couldn't find your page.
			</p>
			<div
				class="mt-5 flex flex-col items-center justify-center gap-2 sm:flex-row sm:gap-3"
			>
				<router-link
					class="endava inline-flex w-full items-center justify-center gap-x-3 rounded-md border border-transparent px-4 py-3 text-center text-sm font-medium text-white transition focus:outline-none focus:ring-2 focus:ring-endava-600 focus:ring-offset-2 focus:ring-offset-white sm:w-auto"
					to="/"
					><svg
						class="h-2.5 w-2.5"
						width="16"
						height="16"
						viewBox="0 0 16 16"
						fill="none"
					>
						<path
							d="M11.2792 1.64001L5.63273 7.28646C5.43747 7.48172 5.43747 7.79831 5.63273 7.99357L11.2792 13.64"
							stroke="currentColor"
							stroke-width="2"
							stroke-linecap="round"
						/>
					</svg>
					Go back Home
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
