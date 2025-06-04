function setup(){

    let config;

    function servicePointsConfig(){
        config.timeSlotService = timeSlotService;
        config.classService = classService;
        config.classesService = classesService;
        config.disciplineService = disciplineService;
        config.disciplineAllocationService = disciplineAllocationService;
        config.roomTypeService = roomTypeService;
        config.roomService = roomService;
        config.teacherService = teacherService;
        config.semiyearService = semiyearService;
        config.groupService = groupService;

        return config;
    }

    function envConfig(){
        config.headers = {Host: "localhost:4567"};
        config.baseURL = "https://" + config.headers.Host + "/" + config.version;
        return config;
    }

    config = {
        headers: {Host: ""},
        baseURL: "",
        version: "db",
        ssl: true
    };

    config = envConfig();
    config = servicePointsConfig();

    return config;
}