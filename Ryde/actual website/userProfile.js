function changeNumber() {
    $("#phone_number").text($("#new_phone_number").val());
    $("#phoneNumberModal").modal("hide");
}

function changeMake() {
    $("#car_make").text($("#new_car_make").val());
    $("#carMakeModal").modal("hide");
}

function changeModel() {
    $("#car_model").text($("#new_car_model").val());
    $("#carModelModal").modal("hide");
}

function changeColor() {
    $("#car_color").text($("#new_car_color").val());
    $("#carColorModal").modal("hide");
}

function changeSeats() {
    $("#car_seats").text($("#new_car_seats").val());
    $("#carSeatsModal").modal("hide");
}