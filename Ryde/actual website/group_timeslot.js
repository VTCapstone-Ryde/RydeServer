var data = [
    {
        "Timeslot": "Event 1",
        "Time": "5 P.M.-9 P.M.",
        "Drivers": "List of drivers",
        "Password": "jfosiej"
    },
    {
        "Timeslot": "Event 2",
        "Time": "6 P.M.-10 P.M.",
        "Drivers": "List of drivers",
        "Password": "irdjgw"
    },
    {
        "Timeslot": "Event 3",
        "Time": "7 P.M.-11 P.M.",
        "Drivers": "List of drivers",
        "Password": "isejfb"
    },
    {
        "Timeslot": "Event 4",
        "Time": "8 P.M.-12 A.M.",
        "Drivers": "List of drivers",
        "Password": "mgeije"
    }
];

$(function () {
    $('#table').bootstrapTable({
        data: data
    });
});