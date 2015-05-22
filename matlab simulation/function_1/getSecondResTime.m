function [ second_res_time ] = getSecondResTime( second_node_info1, second_node_info2 )
%GETSECONDRESTIME 此处显示有关此函数的摘要
% function [ second_node_info1, second_node_info2 ] = getSecondResTime( second_node_info ): get
% total response time of second phase.

% Input:
%       second_node_info1: list of nodes/edge that make up second phase.
%       second_node_info2: list of nodes/edge that make up second phase.
%       notice: second_node_info1 and second_node_info2 is parallel in
%       second phase. Thus, the larger one will be returned.
% Output:
%       second_res_time: the total response of second phase
second_res_time = 0;

second_res_time1 = sum(second_node_info1(:, 1));
second_res_time2 = sum(second_node_info2(:, 1));
second_res_time = max([second_res_time1, second_res_time2]);
end

