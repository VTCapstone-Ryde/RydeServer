var data = [
    {
        "Timeslot": "Event 1",
        "Group": "Group 1",
        "Time": "5 P.M.-9 P.M.",
        "Date": "04/11/2016",
        "Drivers": "List of drivers",
        "Password": "jfosiej"
    },
    {
        "Timeslot": "Event 2",
        "Group": "Group 1",
        "Time": "6 P.M.-10 P.M.",
        "Date": "04/15/2016",
        "Drivers": "List of drivers",
        "Password": "irdjgw"
    },
    {
        "Timeslot": "Event 3",
        "Group": "Group 1",
        "Time": "7 P.M.-11 P.M.",
        "Date": "04/23/2016",
        "Drivers": "List of drivers",
        "Password": "isejfb"
    },
    {
        "Timeslot": "Event 4",
        "Group": "Group 2",
        "Time": "8 P.M.-12 A.M.",
        "Date": "04/24/2016",
        "Drivers": "List of drivers",
        "Password": "mgeije"
    }
];

$(function () {
    $('#table').bootstrapTable({
        data: data
    });
});