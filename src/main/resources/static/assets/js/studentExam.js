const app = Vue.createApp({
    data() {
        return {
            isLoaded: false,
            studentId: '',
            bheId: '',
            examId: '',
            exam: {},
            bhe: {},
            examStartTime: null,
            studentHasExam: {},
            isExamEnd: false,
            isStarted: false,
            isSubmitted: false,
            studentAnswerList: [],
            displayDays: 0,
            displayHours: 0,
            displayMinutes: 0,
            displaySeconds: 0,
            studentScore: 0
        }
    },
    computed: {
        _seconds: () => 1000,
        _minutes() {
            return this._seconds * 60;
        },
        _hours() {
            return this._minutes * 60;
        },
        _days() {
            return this._hours * 24;
        }
    },
    methods: {
        formatNum: num => (num < 10 ? '0' + num : num),
        showExamStartTime() {
            const timer = setInterval(() => {
                const now = new Date();
                let [year, month, day, hour, minute] = this.bhe.end;
                const end = new Date(year, month - 1, day, hour, minute);
                const distance = end.getTime() - now.getTime();
                if (distance <= 0) {
                    clearInterval(timer);
                    if (this.studentAnswerList.length > 0 && !this.isSubmitted) {
                        this.handleSubmit();
                    }
                    this.isExamEnd = true;
                    this.isLoaded = true;
                    return;
                }
                const days = Math.floor((distance / this._days));
                const hours = Math.floor((distance % this._days) / this._hours);
                const minutes = Math.floor((distance % this._hours) / this._minutes);
                const seconds = Math.floor((distance % this._minutes) / this._seconds);
                this.displayMinutes = this.formatNum(minutes);
                this.displaySeconds = this.formatNum(seconds);
                this.displayHours = this.formatNum(hours);
                this.displayDays = this.formatNum(days);
                this.isLoaded = true;
            }, 1000)
        },
        getBheData() {
            axios
                .get(`http://localhost:9090/api/studentExam/getBheById/${this.bheId}`)
                .then(res => {
                    this.bhe = { ...res.data };
                    this.exam = { ...this.bhe.exam };
                    let [year, month, day, hour, minute] =  this.bhe.start;
                    this.examStartTime = new Date(year, month, day, hour, minute).toLocaleString();
                })
                .then(() => {
                    let [year, month, day, hour, minute] = this.bhe.end;
                    let remainingTime = new Date(year, month - 1, day, hour, minute).getTime() - new Date().getTime();
                    if (remainingTime <= 0) {
                        this.isExamEnd = true;
                    }
                    const startDate = setInterval(() => {
                        let [year, month, day, hour, minute] = this.bhe.start;
                        let remainingTimeToStart = new Date(year, month - 1, day, hour, minute).getTime() - new Date().getTime();
                        if (remainingTimeToStart <= 0) {
                            this.isStarted = true;
                            clearInterval(startDate);
                        }
                    }, 1000);
                })
                .catch(error => console.log(error));
        },
        getStudentHasExam() {
            axios
                .get(`http://localhost:9090/api/studentExam/getStudentHasExam/${this.studentId}/${this.examId}`)
                .then(res => {
                    this.studentHasExam = { ...res.data };
                    if (res.data != '') {
                        this.isSubmitted = true;
                        this.studentScore = this.studentHasExam.mark;
                    }
                })
                .catch(error => console.log(error));
        },
        handleSubmit() {
            if (this.studentAnswerList.length > 0) {
                let rightAnswerList = [];
                this.exam.questions.forEach(question => {
                    rightAnswerList = [...rightAnswerList, [question.correctAnswer, question.mark]];
                });
                for (let i = 0; i < rightAnswerList.length; i++) {
                    if (rightAnswerList[i][0] == this.studentAnswerList[i]) {
                        this.studentScore = this.studentScore + rightAnswerList[i][1];
                    }
                }
                console.log(this.studentScore);
                axios.post(`http://localhost:9090/api/studentExam/addStudentHasExam`, null, {
                    params: {
                        studentId: this.studentId,
                        examId: this.examId,
                        score: this.studentScore
                    }
                });
                this.isSubmitted = true;
            }
        },

    },
    mounted() {
        this.studentId = document.getElementById('studentId').value;
        this.bheId = document.getElementById('bheId').value;
        this.examId = document.getElementById('examId').value;
        this.showExamStartTime();
        this.getBheData();
        this.getStudentHasExam();
    }
})
app.mount('#exam')