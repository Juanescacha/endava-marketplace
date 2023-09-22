<script setup>
defineProps({
	message: {
		type: String,
		default: null,
	},
	color: {
		type: String,
		required: true,
	},
});
</script>

<template>
	<div
		v-if="message"
		:class="`border-${color}-500 border bg-${color}-200 px-2 py-4 text-${color}-500`"
	>
		{{ message }}
	</div>
</template>
