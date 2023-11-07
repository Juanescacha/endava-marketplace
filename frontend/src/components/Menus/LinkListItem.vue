<script setup>
	defineProps({
		redirectsTo: {
			type: String,
			required: true,
		},
		colorClasses: {
			type: String,
			default: "text-gray-800 hover:bg-gray-100 active:bg-gray-200",
		},
	});
</script>

<template>
	<router-link
		:to="redirectsTo"
		class="flex h-full w-full items-center gap-x-3.5 rounded-md px-3 py-2 text-sm"
		:class="colorClasses"
	>
		<slot></slot>
	</router-link>
</template>
