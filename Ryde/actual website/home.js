var data_groups = [
    {
        "Group": "Beta Balci Beta"
    },
    {
        "Group": "Alpha Osman Chi"
    },
    {
        "Group": "Capstone Sigma"
    },
    {
        "Group": "Osman Kappa Theta"
    },
    {
        "Group": "CS 4704"
    },
    {
        "Group": "Balci Kappa Theta"
    }
];

$(function () {
    $('#groups').bootstrapTable({
        data: data_groups
    });
});

var data_active = [
    {
        "Group": "Beta Balci Beta",
        "Time": "7PM - 2AM"
    },
    {
        "Group": "CS 4704",
        "Time": "5PM - 8 PM"
    },
    {
        "Group": "TAD 1",
        "Time": "10PM - 4AM"
    }
];

$(function () {
    $('#active_groups').bootstrapTable({
        data: data_active
    });
});