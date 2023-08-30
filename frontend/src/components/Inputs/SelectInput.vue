<script setup>
import { capitalizeFirstLetter } from "../../utils";

defineProps({
	id: {
		type: String,
		required: true,
	},
	title: String,
	options: Array,
	dynamicClass: String,
});

const emit = defineEmits(["selectionChanged"]);

const handleOptionChange = $ev => {
	emit("selectionChanged", $ev);
};
</script>

<template>
	<select
		:id="id"
		:title="title"
		class="rounded border border-zinc-400 px-2 py-3"
		:class="dynamicClass"
		@input="handleOptionChange"
	>
		<option
			v-for="(item, idx) in options"
			:key="item.id + item.name"
			:value="item.name"
			:disabled="idx === 0 ? true : false"
			:selected="idx === 0 ? true : false"
		>
			{{ capitalizeFirstLetter(item.name) }}
		</option>
	</select>
</template>
