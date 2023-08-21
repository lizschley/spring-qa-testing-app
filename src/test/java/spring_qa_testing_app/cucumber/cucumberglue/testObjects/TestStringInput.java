package spring_qa_testing_app.cucumber.cucumberglue.testObjects;

public class TestStringInput {

    public String getInvalidIdUniquenessTestInput() {
        return "{\"id\":99,\"firstName\":\"Jane\",\"lastName\":\"Austen\",\"dates\":\"1775-12-16 to 1817-7-18\", " +
                "\"personNote\":\"BC\",\"quotes\":[{\"quote\":\"Timid men prefer the calm of despotism to the tempestuous sea" +
                " of liberty.\",\"quoteNote\":\"really Thomas Jefferson\"}]}";
    }

    public String getHappyPathValidCreatePersonTestInput() {
        return "{\"id\":333,\"firstName\":\"Tamora\",\"lastName\":\"Pierce\",\"dates\":\"1953-12-13 to ?\"," +
                "\"personNote\":\"All quotes are from fictional characters.\",\"quotes\":[{\"quote\":\"It's harder to" +
                " heal than it is to kill.\",\"quoteNote\":\"Tamora Pierce (2009). Alanna: The First Adventure, p.12, " +
                "Simon and Schuster\"},{\"quote\":\"I am not wise, but I can always learn.\",\"quoteNote\":\"Tamora " +
                "Pierce (2011). The Woman Who Rides Like a Man, p.178, Simon and Schuster\"}]}";
    }

    public String getHappyPathValidCreateQuotesOnlyTestInput() {
        return "{\"id\":3,\"firstName\":\"Jane\",\"lastName\":\"Austen\",\"dates\":\"1775-12-16 to 1817-7-18\"," +
                "\"personNote\":\"All quotes are from fictional characters.\",\"quotes\":[{\"quote\":\"For what do we " +
                "live, but to make sport for our neighbours and laugh at them in our turn?\"," +
                "\"quoteNote\":\"Mr. Bennet in Pride and Prejudice\"}]}";
    }
}
