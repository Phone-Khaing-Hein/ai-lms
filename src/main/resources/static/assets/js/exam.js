const app = new Vue({
  el: "#app",
  data: {
    fullmark: "",
    isEmpty: null,
    examTile: "",
    courseId: "",
    questionList: [
      {
        question: "",
        correctAnswer: "",
        mark: "",
        answers: [
          {
            answer: "",
          },
        ],
      },
    ],
  },
  methods: {
    addQuestion(index) {
      this.questionList = [
        ...this.questionList,
        {
          contain: true,
          question: "",
          correctAnswer: "",
          mark: "",
          answers: [
            {
              answer: "",
            },
          ],
        },
      ];
    },
    deleteQuestion(index) {
      if (this.questionList.length > 1) {
        const values = [...this.questionList];
        values.splice(index, 1);
        this.questionList = values;
      }
    },
    addAnswer(index) {
      this.questionList[index].answers = [
        ...this.questionList[index].answers,
        {
          answer: "",
        },
      ];
    },
    deleteAnswer(index, answerIndex) {
      if (this.questionList[index].answers.length > 1) {
        let answers = [...this.questionList[index].answers];
        answers.splice(answerIndex, 1);
        this.questionList[index].answers = answers;
      }
    },
    createExam() {
      this.isEmpty = false;
      this.questionList.forEach((question) => {
        question.answers.forEach((answer) => {
          if (answer.answer.replace(/\s/g, "").length == 0) {
            this.isEmpty = true;
            Swal.fire("Please Fill All Radio Options!", "", "info");
          }
        });

        if (
          question.question.replace(/\s/g, "").length == 0 ||
          question.correctAnswer.replace(/\s/g, "").length == 0 ||
          question.mark.length == 0
        ) {
          this.isEmpty = true;
          Swal.fire("Please Fill All Question Forms!", "", "info");
        } else if (
          this.courseId.replace(/\s/g, "").length == 0 ||
          this.examTile.replace(/\s/g, "").length == 0
        ) {
          this.isEmpty = true;
          Swal.fire("Please Fill All Input Forms!", "", "info");
        }
      });

      if (!this.isEmpty) {
        let fullMark = 0;
        this.questionList.forEach((question) => {
          fullMark = fullMark + question.mark;
          this.fullmark = fullMark;
        });
        let data = {
          courseId: this.courseId,
          title: this.examTile,
          fullmark: this.fullmark,
          questions: this.questionList,
        };
        console.log(data);
        axios
          .post("http://localhost:9090/admin/exam-create", data)
          .then(() => {
            Swal.fire("Successfully Created!", "", "success").then(
              () => (window.location = "http://localhost:9090/admin/exam-list")
            );
          })
          .catch((error) => console.log(error));
      }
    },
  },
});
