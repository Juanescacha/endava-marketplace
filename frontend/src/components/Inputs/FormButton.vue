<script setup>
defineProps({
	btnType: {
		type: String,
		default: "button",
	},
	text: {
		type: String,
		required: true,
	},
});
</script>

<template>
	<button
		:type="btnType"
		class="endava mt-4 h-12 w-48"
	>
		{{ text }}
	</button>
</template>
