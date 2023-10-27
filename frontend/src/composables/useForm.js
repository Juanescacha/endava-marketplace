export default function useForms() {
	const validateTextInput = ($event, formData) => {
		const input = $event.target.id;
		const form = formData;
		form[input].valid = $event.target.value.length > 0;
	};

	const validateNumericInput = ($event, formData) => {
		const input = $event.target.id;
		const form = formData;
		form[input].valid = Number($event.target.value) > 0;
	};

	const handleInputUpdate = ($event, formData) => {
		const form = formData;
		form[$event.target.id].value = $event.target.value;
	};

	const handleNumericInputUpdate = ($event, formData) => {
		const num = Number($event.target.value);
		const form = formData;
		if (Number.isNaN(num)) {
			return;
		}
		form[$event.target.id].value = num;
	};

	const handleMediaUpdate = (media, formData) => {
		const form = formData;
		form.media.value = media;
		if (form.media.value.length > 0) {
			form.media.valid = true;
		}
	};

	const handleSelectUpdate = ($event, formData) => {
		const optionId =
			$event.target.options[$event.target.selectedIndex].dataset.id;

		const modifiedEvent = {
			target: { id: $event.target.id, value: Number(optionId) },
		};
		handleInputUpdate(modifiedEvent, formData);

		const form = formData;
		form[$event.target.id].valid = true;
	};

	const handleConditionUpdate = ({ rating }, formData) => {
		if (rating > 0) {
			const form = formData;
			form.condition.value = rating;
			form.condition.valid = true;
		}
	};

	const removeNullsFromImages = images => {
		let auxMedia = images;
		if (images.includes(null)) {
			auxMedia = images.slice(0, images.indexOf(null));
		}

		return auxMedia;
	};

	const isValidForm = formData => {
		const inputValues = Object.values(formData);
		const invalidInput = inputValues.find(input => !input.valid);

		return !invalidInput;
	};

	return {
		validateTextInput,
		validateNumericInput,
		handleInputUpdate,
		handleNumericInputUpdate,
		handleMediaUpdate,
		handleSelectUpdate,
		handleConditionUpdate,
		removeNullsFromImages,
		isValidForm,
	};
}
