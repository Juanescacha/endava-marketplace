<script setup>
	import { ref } from "vue";

	const inputValueLength = ref(0);

	const emit = defineEmits(["inputChanged", "focusout"]);
	const props = defineProps({
		name: {
			type: String,
			required: true,
		},
		max: {
			type: Number,
			default: 500,
		},
		placeholder: {
			type: String,
		},
	});

	const handleInputChange = $event => {
		inputValueLength.value = $event.target.value.length;
		emit("inputChanged", $event);
	};

	const handleFocusOut = $event => {
		emit("focusout", $event);
	};
</script>

<template>
	<div>
		<p class="float-right text-sm text-gray-500">
			{{ `(${inputValueLength}/${max})` }}
		</p>
		<textarea
			:id="name"
			:placeholder="placeholder"
			class="w-full resize-y rounded border border-zinc-400 px-2 py-3"
			@input="handleInputChange"
			@focusout="handleFocusOut"
		/>
	</div>
</template>
