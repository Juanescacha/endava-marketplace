export default function useForms() {
	const validateTextInput = ($event, formData) => {
		const input = $event.target.id;
		const form = formData;
		form[input].valid = $event.target.value.length > 0;
	};

	const validateNumerciInput = ($event, formData) => {
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

	const validateProductDetail = ($event, formData, displayMsg) => {
		if ($event.target.value.length > 500) {
			displayMsg("The detail can't have more than 500 characters", "red");
		} else {
			validateTextInput($event, formData);
		}
	};

	const handleConditionUpdate = ({ rating }, formData) => {
		if (rating > 0) {
			const form = formData;
			form.condition.value = rating;
			form.condition.valid = true;
		}
	};

	const isValidForm = formData => {
		const inputValues = Object.values(formData);
		const invalidInput = inputValues.find(input => !input.valid);

		return !invalidInput;
	};

	return {
		validateTextInput,
		validateNumerciInput,
		handleInputUpdate,
		handleNumericInputUpdate,
		handleMediaUpdate,
		handleSelectUpdate,
		validateProductDetail,
		handleConditionUpdate,
		isValidForm,
	};
}
