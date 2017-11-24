class ListItem extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            comments: [],
            commentBoxContent: '',
            noteUrl: this.getHostUrl() + "/comments/" + this.getNoteId()
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    getNoteId() {
        var url = window.location.href
        var n = url.lastIndexOf('/');
        return url.substring(n + 1);
    }

    getHostUrl() {
        return window.location.protocol + "//" +  window.location.host
    }

    handleSubmit(event) {
        this.setState({
            comments: this.state.comments.concat([<ListItem comment={this.state.commentBoxContent}></ListItem>])
        });
        event.preventDefault();
        event.target.reset();
        this.saveCommentToDb(this.buildComment());
    }

    buildComment() {
        return {
            "noteId": this.getNoteId(),
            "parentCommentId": this.props.id,
            "commentBody": this.state.commentBoxContent
        }
    }

    saveCommentToDb(comment) {
        $.ajax({
            url: this.getHostUrl() + "/comments/" + this.getNoteId(),
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(comment),

        });
    }

    handleChange(event) {
        this.setState({commentBoxContent: event.target.value});
    }

    render() {
        return (
            <tr>
                <h4>{this.props.comment}</h4>
                <form onSubmit={this.handleSubmit} method="post">
                    <label>
                        <input type="text" value={this.state.commentBoxContent} onChange={this.handleChange} />
                    </label>
                    <input type="submit" value="Submit" />
                </form>
                <table>{this.state.comments}</table>
            </tr>
        );
    }
}

class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            existingComments: [],
            noteName: '',
            noteUrl: this.getHostUrl() + "/comments/" + this.getNoteId()
        };
        this.loadInitialComments();
    }

    getNoteId() {
        var url = window.location.href
        var n = url.lastIndexOf('/');
        return url.substring(n + 1);
    }

    getHostUrl() {
        return window.location.protocol + "//" +  window.location.host
    }

    loadInitialComments() {
        if(this.state.existingComments.length > 0) {
            return;
        }
        var self = this;
        var startingComments = [];
        $.ajax({
            url: this.state.noteUrl,
            dataType: "json",
        }).then(function (data) {
            for (let oldComment in data["comments"]) {
                startingComments = startingComments.concat(([<ListItem comment={data["comments"][oldComment]["commentBody"]} id={data["comments"][oldComment]["id"]}></ListItem>]))
            }
            self.setState({
                existingComments: startingComments,
                noteName: data["name"]
            });
        });
    }

    render() {
        return (
            <div>
                <table>
                    <h1>{this.state.noteName}</h1>
                    <ListItem comment=''></ListItem>
                    {this.state.existingComments}
                </table>
            </div>
        );
    }
}

ReactDOM.render(<App />, document.getElementById('root'));