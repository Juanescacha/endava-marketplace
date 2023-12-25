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
			:src="user.image"
			:alt="`Profile picture of ${user.name}`"
		/>
		<div>
			<h1 class="my-3 text-xl font-semibold">{{ user.name }}</h1>
			<p>
				{{ user.email }}
			</p>
			<router-link
				v-if="user.isAdmin"
				to="/admin-panel"
				class="endava mt-4 block w-full p-2 text-center"
			>
				Admin Panel
			</router-link>
		</div>
	</div>
</template>
