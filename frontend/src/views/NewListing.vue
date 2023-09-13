<script setup>
import { reactive, onMounted } from "vue";
import { makePostRequest } from "../utils/axios";
import useNotification from "../composables/useNotification";
import useForm from "../composables/useForm";
import SelectInput from "../components/Inputs/SelectInput.vue";
import StarsInput from "../components/Inputs/StarsInput.vue";
import ImageInputList from "../components/Inputs/ImageInputList.vue";
import FormButton from "../components/Inputs/FormButton.vue";
import NotificationBox from "../components/NotificationBox.vue";

const categories = reactive([]);
const formData = reactive({
	name: { value: "", valid: false },
	detail: { value: "", valid: false },
	category: { value: "", valid: false },
	price: { value: 0, valid: false },
	condition: { value: 0, valid: false },
	stock: { value: 0, valid: false },
	media: { value: "", valid: false },
});

const { showMsg, msgColor, displayMsg } = useNotification();
const {
	validateTextInput,
	validateNumerciInput,
	handleInputUpdate,
	handleNumericInputUpdate,
	handleMediaUpdate,
	handleSelectUpdate,
	validateProductDetail,
	handleConditionUpdate,
	isValidForm,
} = useForm();

onMounted(() => {
	// TODO load dinamically
	categories.push({ id: 1, name: "technology" });
	categories.push({ id: 1, name: "appliances" });
	categories.push({ id: 1, name: "clothing" });
});

// useForms?
const organizePostPetition = () => {
	let auxMedia = formData.media.value;
	if (auxMedia.includes(null)) {
		auxMedia = auxMedia.slice(0, auxMedia.indexOf(null));
	}
	const postData = {
		seller: {
			id: 1, // TODO load dinamically
		},
		category: {
			id: 1,
		},
		status: {
			id: 1, // TODO load dinamically
		},
		name: formData.name.value,
		detail: formData.detail.value,
		condition: formData.condition.value,
		price: formData.price.value,
		stock: formData.stock.value,
		media: "auxMedia", // TODO update to BE specifications
	};
	return postData;
};

const handleFormSubmit = () => {
	if (isValidForm(formData)) {
		const postData = organizePostPetition();
		let url = import.meta.env.VITE_API_URL;
		url += "/api/listings/post";

		makePostRequest(url, postData).then(response => {
			if (response?.err) {
				displayMsg(response.err, "red");
				return;
			}
			displayMsg(response.msg, "green");
			// TODO redirect to other page
		});
	} else {
		displayMsg("All fields are obligatory", "red");
	}
};
</script>

<template>
	<main class="mb-10 ml-8 mt-24">
		<h1>Publish new product</h1>
		<form
			class="mx-10 my-5 grid w-3/4 grid-cols-2 gap-2"
			@submit.prevent="handleFormSubmit"
		>
			<input
				id="name"
				placeholder="Product name"
				title="Product name"
				class="col-span-2 rounded border border-zinc-400 px-2 py-3"
				autocomplete="off"
				@input="handleInputUpdate($event, formData)"
				@focusout="validateTextInput($event, formData)"
			/>
			<textarea
				id="detail"
				placeholder="Product detail"
				title="Product detail"
				class="col-span-2 rounded border border-zinc-400 px-2 py-3"
				@input="handleInputUpdate($event, formData)"
				@focusout="validateProductDetail($event, formData, displayMsg)"
			></textarea>
			<select-input
				id="category"
				title="Category"
				:options="categories"
				@selection-changed="handleSelectUpdate($event, formData)"
			/>
			<input
				id="price"
				type="number"
				placeholder="Price"
				title="Product price"
				class="rounded border border-zinc-400 px-2 py-3"
				@input="handleNumericInputUpdate($event, formData)"
				@focusout="validateNumerciInput($event, formData)"
			/>
			<input
				id="stock"
				type="number"
				placeholder="Quantity"
				title="Number of units"
				class="col-span-1 rounded border border-zinc-400 px-2 py-3"
				@input="handleNumericInputUpdate($event, formData)"
				@focusout="validateNumerciInput($event, formData)"
			/>
			<stars-input
				label="Condition"
				:increment="0.5"
				color="#DE411B"
				@rating-updated="handleConditionUpdate($event, formData)"
			/>
			<image-input-list
				@update-media-list="handleMediaUpdate($event, formData)"
			/>
			<form-button
				btn-type="submit"
				text="Publish"
			/>
			<notification-box
				:message="showMsg"
				:color="msgColor"
			/>
		</form>
	</main>
</template>
