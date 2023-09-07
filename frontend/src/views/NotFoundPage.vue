<template>
	<div class="flex h-screen">
		<div
			class="mx-auto my-auto flex h-fit w-auto flex-col items-center gap-2"
		>
			<h1>404 page not found</h1>
			<p>Sorry, the page you are looking for doesn't exist.</p>
			<router-link
				to="/"
				class="inline-flex items-center justify-center gap-2 rounded-md border bg-endava-400 px-4 py-3 text-sm font-semibold text-white shadow-sm transition-all hover:bg-endava-500 active:bg-endava-600"
			>
				Return Home
			</router-link>
		</div>
	</div>
</template>
