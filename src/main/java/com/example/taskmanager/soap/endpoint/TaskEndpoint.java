package com.example.taskmanager.soap.endpoint;

import com.example.tasks.GetTaskRequest;
import com.example.tasks.GetTaskResponse;
import com.example.tasks.Task;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class TaskEndpoint {

    private static final String NAMESPACE_URI = "http://example.com/tasks";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetTaskRequest")
    @ResponsePayload
    public GetTaskResponse getTask(@RequestPayload GetTaskRequest request) {
        GetTaskResponse response = new GetTaskResponse();

        // Mock Task data
        Task task = new Task();
        task.setId(request.getTaskId());
        task.setTitle("Sample Task");
        task.setDescription("This is a sample task.");
        task.setCompleted(false);

        response.setTask(task);
        return response;
    }
}
