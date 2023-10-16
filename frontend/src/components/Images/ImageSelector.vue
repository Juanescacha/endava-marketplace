<script setup>
	import { onUpdated, ref } from "vue";
	import ImageSelectorList from "./ImageSelectorList.vue";

	const props = defineProps({
		images: {
			type: Array,
			required: true,
		},
		styles: {
			type: String,
		},
	});

	const currentImage = ref(props.images[0]);

	const handleThumbnailClick = image => {
		currentImage.value = image;
	};

	onUpdated(() => {
		currentImage.value = props.images[0];
	});
</script>

<template>
	<div
		class="flex h-[65vh] flex-col lg:flex-row-reverse"
		:class="styles"
	>
		<div
			class="flex justify-center overflow-hidden rounded border border-gray-300 p-2 lg:w-4/5"
		>
			<img
				:src="currentImage"
				class="h-full rounded-lg object-contain"
			/>
		</div>
		<image-selector-list
			styles="flex gap-4 m-2 lg:flex-col"
			:images="images"
			@image-selected="handleThumbnailClick"
		/>
	</div>
</template>
