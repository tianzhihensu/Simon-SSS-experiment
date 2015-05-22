function [ third_res_time ] = getThridResTime( third_node_info )
%GETTHIRDRESTIME 此处显示有关此函数的摘要
% function [ third_res_time ] = getThirdResTime( third_node_info ):
% generate the total response time of the third phase
%
% Input:
%       third_node_info: list of nodes that make up the third phase.
% Output:
%       third_res_time: total response time of the third phase
third_res_time = 0;

third_res_time = sum(third_node_info(:, 1));

end

