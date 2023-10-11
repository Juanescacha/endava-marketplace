<script setup>
	defineProps({
		user: {
			type: String,
			required: true,
		},
		innerText: {
			type: String,
			default: "Let's chat!",
		},
	});
</script>

<template>
	<a
		:href="`https://teams.microsoft.com/l/chat/0/0?users=${user}`"
		target="_blank"
		rel="noopener noreferrer"
		class="inline-block w-auto rounded-lg bg-[#4f52b2] px-4 py-3 text-white shadow-md hover:bg-[#444791] active:bg-[#383966]"
	>
		{{ innerText }}
	</a>
</template>
