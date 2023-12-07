<script setup>
	defineProps({
		size: {
			type: Number,
			default: 15,
			validator(value) {
				return value > 0;
			},
		},
	});
</script>

<template>
	<svg
		:width="size"
		:height="size * 2"
		version="1.1"
		xmlns="http://www.w3.org/2000/svg"
	>
		<line
			:x1="0"
			:x2="0"
			:y1="0"
			:y2="size"
			stroke="black"
			stroke-width="2"
		/>
		<line
			:x1="0"
			:x2="size"
			:y1="size"
			:y2="size"
			stroke="black"
			stroke-width="1"
		/>
	</svg>
</template>
