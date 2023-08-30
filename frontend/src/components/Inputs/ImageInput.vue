<script setup>
import { ref } from "vue";

const imageObj = ref(null);
const imagePreview = ref(null);

const props = defineProps({
	id: {
		type: String,
		required: true,
	},
});
const emit = defineEmits(["imageUploaded"]);

const extractIndexFromMediaString = mediaStr => mediaStr.slice(5);

const setPreviewImage = () => {
	const reader = new FileReader();
	reader.readAsDataURL(imageObj.value);
	reader.onload = e => {
		imagePreview.value = e.target.result;
	};
};

const handleInputClick = () => {
	document.getElementById(props.id).click();
};

const handleImageUpload = $ev => {
	let idx = extractIndexFromMediaString($ev.target.id);
	idx = Number(idx);

	if (Number.isNaN(idx)) {
		throw new Error("Please format the media input ids as 'media<idx>'");
	}

	[imageObj.value] = $ev.target.files;
	setPreviewImage();

	emit("imageUploaded", {
		idx,
		imageObj: imageObj.value,
	});
};
</script>

<template>
	<input
		type="file"
		:id="id"
		class="hidden"
		title="Upload image"
		accept="image/png, image/jpeg"
		@input="handleImageUpload"
	/>
	<button
		class="h-full w-full"
		type="button"
		title="Upload image"
		@click="handleInputClick"
	>
		<img
			v-if="imageObj"
			:src="imagePreview"
			:alt="`Image ${id} of the product`"
		/>
		<div
			v-if="!imageObj"
			class=""
		>
			+
		</div>
	</button>
</template>
