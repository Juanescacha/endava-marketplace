<script setup>
	import { capitalizeFirstLetter } from "@/utils/strings";

	defineProps({
		id: {
			type: String,
			required: true,
		},
		title: {
			type: String,
			required: true,
		},
		options: {
			type: Array,
			required: true,
		},
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
			disabled
			selected
		>
			{{ title }}
		</option>
		<option
			v-for="item in options"
			:key="item.id + item.name"
			:value="item.name"
			:data-id="item.id"
		>
			{{ capitalizeFirstLetter(item.name) }}
		</option>
	</select>
</template>
