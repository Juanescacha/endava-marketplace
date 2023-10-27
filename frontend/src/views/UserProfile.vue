<script setup>
	import { useUserStore } from "@/stores/user";

	const user = useUserStore();
</script>

<template>
	<div
		class="m-3 flex w-full flex-col items-center justify-center gap-6 lg:flex-row"
	>
		<img
			class="h-auto w-[60vw] max-w-[16rem] rounded-full lg:w-1/2"
			src="https://images.unsplash.com/photo-1568602471122-7832951cc4c5?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=facearea&facepad=2&w=320&h=320&q=80"
			:alt="`Profile picture of ${user.name}`"
		/>
		<div>
			<h1 class="my-3 text-xl font-semibold">{{ user.name }}</h1>
			<p>
				{{ user.email }}
			</p>
			<button
				v-if="user.isAdmin"
				class="endava mt-4 w-full p-2"
			>
				Admin panel
			</button>
		</div>
	</div>
</template>
