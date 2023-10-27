<script setup>
	defineProps({
		message: {
			type: String,
			default: null,
		},
		color: {
			type: String,
			default: "gray",
		},
	});

	const colorVariants = {
		red: "bg-red-200 text-red-500",
		green: "bg-green-200 text-green-500",
		gray: "bg-gray-200 text-gray-500",
	};
</script>

<template>
	<div
		v-if="message"
		class="rounded-md p-4 drop-shadow-md"
		:class="colorVariants[color]"
	>
		{{ message }}
	</div>
</template>
