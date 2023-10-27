<script setup>
	import { ref } from "vue";
	const props = defineProps({
		images: {
			type: Array,
			required: true,
		},
		styles: {
			type: String,
		},
	});

	const emit = defineEmits(["image-selected"]);
	const currentIndex = ref(0);

	const handleThumbnailClick = ($event, idx) => {
		currentIndex.value = idx;
		emit("image-selected", props.images[currentIndex.value]);
	};
</script>

<template>
	<ul :class="styles">
		<li
			v-for="(image, idx) in images"
			:key="image"
			class="h-20 w-20"
		>
			<img
				:src="image"
				:alt="`Image number ${idx + 1}`"
				class="h-full w-auto rounded-lg border object-cover hover:border-2 hover:border-gray-900"
				:class="
					currentIndex === idx
						? 'border-[3px] border-endava-600'
						: 'border-gray-400'
				"
				@click="handleThumbnailClick($event, idx)"
			/>
		</li>
	</ul>
</template>
